#!/bin/sh
# install-java.tcl \
exec tclsh "$0" ${1+"$@"}

package require yaml

# ---------- common utils --------------------------
proc loadYaml {fileName} {
  if {[catch {set dt [::yaml::yaml2dict -file $fileName]} msg o]} {
    puts $msg
    exit 1
  } else {
    return $dt
  }
}

proc sethostname {hn} {
  if {[string length $hn] > 0} {
    exec hostnamectl set-hostname $hn --static
  }
}

proc backupOrigin {fn} {
  if {[file exists $fn]} {
    set of "$fn.origin"
    if {! [file exists $of]} {
      exec cp $fn $of
    }
  }
}

proc setupResolver {nameserver} {
  set resolverFile /etc/resolv.conf
  backupOrigin $resolverFile
  if {[catch {open $resolverFile w} fid o]} {
    puts $fid
    exit 1
  } else {
    puts $fid "nameserver $nameserver"
    close $fid
  }
}

proc openFirewall {prot args} {
  foreach port $args {
    catch {exec firewall-cmd --permanent --zone=public --add-port ${port}/$prot} msg o
  }
  catch {exec firewall-cmd --reload} msg o
}
# ---------- common utils --------------------------

set envfile [lindex $argv 1]
set action [lindex $argv 2]
set envdict [loadYaml $envfile]

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

set software [dict get $envdict software]
set configContent [dict get $software configContent]
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

# setup nameserver
setupResolver [dict get $selfbox dnsServer]
# set hostname
sethostname [dict get $selfbox hostname]
openFirewall tcp $zkports

# for test only
set tob [dict create lines $lines selfbox $selfbox boxes $boxes configFile $configFile]
