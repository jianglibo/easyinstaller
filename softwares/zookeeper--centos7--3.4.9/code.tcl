#!/bin/sh
# install-java.tcl \
exec tclsh "$0" ${1+"$@"}

package require yaml

# insert-common-script-here:classpath:scripts/tcl/shared.tcl

set tarFile zookeeper-3.4.9.tar.gz
set envfile [lindex $argv 1]
set action [lindex $argv 2]
set envdict [loadYaml $envfile]
set remoteFolder [dict get $envdict remoteFolder]

set software [dict get $envdict software]
set configContent [dict get $software configContent]

# self box
set selfbox [dict get $envdict box]

set boxes {}

# As a convention, use last segment of ip address as server id.
# Normalize box.
foreach box [dict get $envdict boxGroup boxes] {
  if {[string length [dict get $box hostname]] > 0} {
    dict set box hostnameOrIp [dict get $box hostname]
  } else {
    dict set box hostnameOrIp [dict get $box ip]
  }

  if {[string length [dict get $box dnsServer]] == 0} {
    dict set box dnsServer [dict get $envdict boxGroup dnsServer]
  }

  dict set box serverid [lindex [split [dict get $box ip] .] end]
  # reset selfbox to normalized box.
  if {[dict get $box ip] eq [dict get $selfbox ip]} {
    set selfbox $box
  }
  lappend boxes $box
}

# regsub -all {\n} $configContent {} configContent
# puts --------------------
set configContentDict [::yaml::yaml2dict -stream $configContent]
set lines {}

dict for {k v} [dict get $configContentDict zkconfig] {
  lappend lines "$k=$v"
}

set zkports [split [dict get $configContentDict zkports] ,]

set configFile [dict get $configContentDict configFile]

# add server.id=xxx lines
foreach box $boxes {
  dict with box {
    lappend lines "server.$serverid=$hostnameOrIp:[join $zkports :]"
  }
}

if {[catch {open $configFile w} fid o]} {
  puts $fid
  exit 1
} else {
  foreach line $lines {
      puts $fid $line
  }
  close $fid
}

set dataDir [dict get $configContentDict zkconfig dataDir]

# create dataDir if not exists.
if {! [file exists $dataDir]} {
  file mkdir $dataDir
}

# write myid file.
if {[catch {open [file join $dataDir myid] w} fid o]} {
  puts $fid
  exit 1
} else {
  puts $fid [dict get $selfbox serverid]
  close $fid
}

set binDir [dict get $configContentDict binDir]

# create dataDir if not exists.
if {! [file exists $binDir]} {
  file mkdir $binDir
}

cd $binDir
exec cp "${remoteFolder}$tarFile" ./
exec tar -zxvf ./$tarFile

# /opt/zookeeper/zookeeper-3.4.9/

# setup nameserver
setupResolver [dict get $selfbox dnsServer]
# set hostname
sethostname [dict get $selfbox hostname]
openFirewall tcp $zkports

# for test only
set tob [dict create lines $lines selfbox $selfbox boxes $boxes configFile $configFile]
