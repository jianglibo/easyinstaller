package require tcltest 2.2
eval ::tcltest::configure $::argv

set ::baseDir [file join [file dirname [info script]] ..]
lappend auto_path $::baseDir


namespace eval ::example::test {

    namespace import ::tcltest::*

    testConstraint X [expr {1}]

    variable SETUP {
      set argv [list --envfile ../envforcodeexec.yaml]
    }
    variable CLEANUP {}

    test parsefile {} -constraints {X win} -setup $SETUP -body {
    source [file join $::baseDir code.tcl]
    } -cleanup $CLEANUP -match exact -result {192.168.33.50}

    # match regexp, glob, exact
    cleanupTests
}

namespace delete ::example::test
