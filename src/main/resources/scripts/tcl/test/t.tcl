package require tcltest 2.2
package require json
package require yaml
eval ::tcltest::configure $::argv

set ::baseDir [file join [file dirname [info script]] ..]
lappend auto_path $::baseDir

namespace eval ::example::test {
    namespace import ::tcltest::*
    testConstraint X [expr {1}]

    test topProperty {} -constraints {X win} -setup {
      } -body {
        source [file normalize [file join $::baseDir shared.tcl]]
        EnvDictNs::initialize [file normalize [file join $::baseDir test envforcodeexec.json]]
        return [dict get $EnvDictNs::envdict remoteFolder]
      } -cleanup {
      } -result {/opt/easyinstaller}


      test boxGroupProperty {} -constraints {X win} -setup {
        } -body {
          source [file normalize [file join $::baseDir shared.tcl]]
          EnvDictNs::initialize [file normalize [file join $::baseDir test envforcodeexec.json]]
          return [dict get $EnvDictNs::boxGroupConfigContent zkconfig tickTime]
        } -cleanup {
        } -result {1999}

      test softwareProperty {} -constraints {X win} -setup {
        } -body {
            source [file normalize [file join $::baseDir shared.tcl]]
            EnvDictNs::initialize [file normalize [file join $::baseDir test envforcodeexec.json]]
            return [dict get $EnvDictNs::softwareConfigContent zkconfig tickTime]
        } -cleanup {
        } -result {1999}

        test filesToUpload {} -constraints {X win} -setup {
          } -body {
              source [file normalize [file join $::baseDir shared.tcl]]
              EnvDictNs::initialize [file normalize [file join $::baseDir test envforcodeexec.json]]
              return [EnvDictNs::getUpload]
          } -cleanup {
          } -result {/opt/easyinstaller/zookeeper-3.4.9.tar.gz}

        test filesToUploadExists {} -constraints {X win} -setup {
          } -body {
              source [file normalize [file join $::baseDir shared.tcl]]
              EnvDictNs::initialize [file normalize [file join $::baseDir test envforcodeexec.json]]
              return [EnvDictNs::getUpload *zookeeper*]
          } -cleanup {
          } -result {/opt/easyinstaller/zookeeper-3.4.9.tar.gz}

        test filesToUploadNotExists {} -constraints {X win} -setup {
          } -body {
              source [file normalize [file join $::baseDir shared.tcl]]
              EnvDictNs::initialize [file normalize [file join $::baseDir test envforcodeexec.json]]
              return [EnvDictNs::getUpload *notexists*]
          } -cleanup {
          } -result {}

      test filesToUploads {} -constraints {X win} -setup {
        } -body {
            source [file normalize [file join $::baseDir shared.tcl]]
            EnvDictNs::initialize [file normalize [file join $::baseDir test envforcodeexec.json]]
            return [EnvDictNs::getUploads]
        } -cleanup {
        } -result {/opt/easyinstaller/zookeeper-3.4.9.tar.gz}


    # match regexp, glob, exact
    cleanupTests
}

namespace delete ::example::test
