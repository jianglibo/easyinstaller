package require tcltest 2.2
eval ::tcltest::configure $::argv

set ::baseDir [file join [file dirname [info script]] ..]
lappend auto_path $::baseDir

source [file normalize [file join $::baseDir .. .. src main resources scripts tcl shared.tcl]]

set tgzFolder [file normalize [file join $::baseDir .. .. tgzFolder]]

namespace eval ::example::test {
    namespace import ::tcltest::*
    testConstraint X [expr {1}]
    test zkLines {} -constraints {} -setup {
      set argv [list -envfile [file join $::baseDir test envforcodeexec.json] -action t]
    } -body {
      source [file join $::baseDir code.tcl]
      return [getZkconfigLines]
    } -cleanup {
    } -result {tickTime=1999 dataDir=/var/lib/zookeeper/ clientPort=2181 initLimit=5 syncLimit=2}

    test serverLines {} -constraints {} -setup {
      set argv [list -envfile [file join $::baseDir test envforcodeexec.json] -action t]
    } -body {
      source [file join $::baseDir code.tcl]
      set boxes [normalizeBoxes]
      return [getServerLines $boxes]
    } -cleanup {
    } -result {server.10=192.168.2.10:2888:3888 server.11=a1.host.name:2888:3888 server.14=a2.host.name:2888:3888}

    test wholeRun {} -constraints {} -setup {
    } -body {
      set argv [list -envfile [file join $::baseDir test envforcodeexec.json] -action t]
      source [file join $::baseDir code.tcl]

      file mkdir $EnvDictNs::remoteFolder

      if {! [file exists [EnvDictNs::getUpload]]} {
        exec cp [file join $tgzFolder [lindex [split [EnvDictNs::getUpload] /] end]] $EnvDictNs::remoteFolder
      }

      set argv [list -envfile [file join $::baseDir test envforcodeexec.json] -action install]
      source [file join $::baseDir code.tcl]

      

      return 0
    } -cleanup {
    } -result {0}

    # match regexp, glob, exact
    cleanupTests
}

namespace delete ::example::test
