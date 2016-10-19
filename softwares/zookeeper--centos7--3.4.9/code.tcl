#!/bin/sh
# install-java.tcl \
exec tclsh "$0" ${1+"$@"}

package require yaml

# insert-common-script-here:classpath:scripts/tcl/shared.tcl

EnvDictNs::initialize [lindex $argv 1]

proc normalizeBoxes {} {
  set boxes {}
  foreach box $EnvDictNs::boxes {
    if {[string length [dict get $box hostname]] > 0} {
      dict set box hostnameOrIp [dict get $box hostname]
    } else {
      dict set box hostnameOrIp [dict get $box ip]
    }

    if {[string length [dict get $box dnsServer]] == 0} {
      dict set box dnsServer [dict get $EnvDictNs::envdict boxGroup dnsServer]
    }

    dict set box serverid [lindex [split [dict get $box ip] .] end]
    # reset selfbox to normalized box.
    if {[dict get $box ip] eq [dict get $EnvDictNs::selfBox ip]} {
      set EnvDictNs::selfBox $box
    }
    lappend boxes $box
  }
  return $boxes;
}

proc getZkconfigLines {} {
  set lines {}
  dict for {k v} [dict get $EnvDictNs::softwareConfigContent zkconfig] {
    lappend lines "$k=$v"
  }
  return $lines
}

proc getServerLines {boxes} {
  set lines {}
  set zkports [split [dict get $EnvDictNs::softwareConfigContent zkports] ,]

  set configFile [dict get $EnvDictNs::softwareConfigContent configFile]

  # add server.id=xxx lines
  foreach box $boxes {
    dict with box {
      lappend lines "server.$serverid=$hostnameOrIp:[join $zkports :]"
    }
  }
  return $lines
}

proc install {} {
    set boxes [normalizeBoxes]
    EnvDictNs::writeFileLines [dict get $EnvDictNs::softwareConfigContent configFile] [concat [getZkconfigLines] [getServerLines $boxes]]
    set dataDir [dict get $EnvDictNs::softwareConfigContent zkconfig dataDir]
    file mkdir $dataDir

    # write myid file.
    EnvDictNs::writeFile [file join $dataDir myid] [dict get $EnvDictNs::selfBox serverid]
    EnvDictNs::untar [dict get $EnvDictNs::softwareConfigContent binDir]

    # setup nameserver
    EnvDictNs::setupResolver [dict get $EnvDictNs::selfBox dnsServer]
    # set hostname
    EnvDictNs::sethostname [dict get $EnvDictNs::selfBox hostname]
    EnvDictNs::openFirewall tcp $zkports
  }

switch -exact -- [lindex $argv 3] {
  install {install}
  t {}
  default {}
}
