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

    variable SETUP {
      set argv [list --envfile [file join $::baseDir test envforcodeexec.yaml]]
    }
    variable CLEANUP {}

    test parsefile {} -constraints {X win} -setup $SETUP -body {
    source [file join $::baseDir code.tcl]
    return $tob
    } -cleanup $CLEANUP -match mm -result {}

    # match regexp, glob, exact
    cleanupTests
}

namespace delete ::example::test
