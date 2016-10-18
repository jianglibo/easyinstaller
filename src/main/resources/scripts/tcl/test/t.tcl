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
        set envfile [readEnvFileJson [file normalize [file join $::baseDir test envforcodeexec.json]]]
        return [dict get $envfile remoteFolder]
      } -cleanup {
      } -result {/opt/easyinstaller}


      test boxGroupProperty {} -constraints {X win} -setup {
        } -body {
          source [file normalize [file join $::baseDir shared.tcl]]
          set envfile [readEnvFileJson [file normalize [file join $::baseDir test envforcodeexec.json]]]
          set bgConfigContent [dict get $envfile boxGroup configContent]
          return [dict get $bgConfigContent zkconfig tickTime]
        } -cleanup {
        } -result {1999}

      test softwareProperty {} -constraints {X win} -setup {
        } -body {
            source [file normalize [file join $::baseDir shared.tcl]]
            set envfile [readEnvFileJson [file normalize [file join $::baseDir test envforcodeexec.json]]]
            set sfConfigContent [dict get $envfile software configContent]
            return [dict get $sfConfigContent zkconfig tickTime]
        } -cleanup {
        } -result {1999}

        test filesToUpload {} -constraints {X win} -setup {
          } -body {
              source [file normalize [file join $::baseDir shared.tcl]]
              set envfile [readEnvFileJson [file normalize [file join $::baseDir test envforcodeexec.json]]]
              return [dict get $envfile software filesToUpload]
          } -cleanup {
          } -result {https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz}

      test ftou {} -constraints {X win} -setup {
        } -body {
            source [file normalize [file join $::baseDir shared.tcl]]
            set envfile [readEnvFileJson [file normalize [file join $::baseDir test envforcodeexec.json]]]
            return [dict get $envfile software ftou]
        } -cleanup {
        } -result {zookeeper-3.4.9.tar.gz}


    # match regexp, glob, exact
    cleanupTests
}

namespace delete ::example::test
