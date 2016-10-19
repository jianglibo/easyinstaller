package require tcltest 2.2
eval ::tcltest::configure $::argv

set ::baseDir [file join [file dirname [info script]] ..]
lappend auto_path $::baseDir

source [file normalize [file join $::baseDir .. .. src main resources scripts tcl shared.tcl]]

::tcltest::customMatch mm mmproc

proc mmproc {expectedResult actualResult} {
  set lines [dict get $actualResult lines]
  regexp {dataDir=([^ ]+)} $lines whole dataDir
  set myidFile [file join $dataDir myid]
  set configFile [dict get $actualResult configFile]

  if {! [file exists $dataDir]} {
    puts "${dataDir} does not exists."
    return 0
  }

  if {! [file exists $myidFile]} {
    puts "${myidFile} does not exists."
    return 0
  }

  if {! [file exists $configFile]} {
    puts "${configFile} does not exists."
    return 0
  }
  return 1
}

namespace eval ::example::test {
    namespace import ::tcltest::*
    testConstraint X [expr {1}]
    test zkLines {} -constraints {X win} -setup {
      set argv [list -envfile [file join $::baseDir test envforcodeexec.json] -action t]
    } -body {
      source [file join $::baseDir code.tcl]
      return [getZkconfigLines]
    } -cleanup {
    } -result {tickTime=1999 dataDir=/var/lib/zookeeper/ clientPort=2181 initLimit=5 syncLimit=2}

    test serverLines {} -constraints {X win} -setup {
      set argv [list -envfile [file join $::baseDir test envforcodeexec.json] -action t]
    } -body {
      source [file join $::baseDir code.tcl]
      set boxes [normalizeBoxes]
      return [getServerLines $boxes]
    } -cleanup {
    } -result {server.10=192.168.2.10:2888:3888 server.11=a1.host.name:2888:3888 server.14=a2.host.name:2888:3888}
    # match regexp, glob, exact
    cleanupTests
}

namespace delete ::example::test
