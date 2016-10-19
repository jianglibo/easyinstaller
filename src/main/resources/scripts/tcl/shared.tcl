package require yaml
package require json

namespace eval EnvDictNs {
  variable envdict

  proc initialize {envfile} {
    variable envdict
    variable remoteFolder
    variable software
    variable boxGroupConfigContent
    variable softwareConfigContent
    variable selfBox
    variable boxes
    set envdict [readEnvFileJson $envfile]
    set remoteFolder [dict get $envdict remoteFolder]
    set software [dict get $envdict software]
    set boxGroupConfigContent [dict get $envdict boxGroup configContent]
    set softwareConfigContent [dict get $envdict software configContent]
    set selfBox [dict get $envdict box]
    set boxes [dict get $envdict boxGroup boxes]

    if {[string index $remoteFolder end] ne "/"} {
      set remoteFolder "${remoteFolder}/"
    }
    set ftou {}
    foreach f [dict get $envdict software filesToUpload] {
      lappend ftou "$remoteFolder[lindex [split $f /] end]"
    }
    dict set envdict software ftou $ftou
  }

  proc untar {dstFolder {tgzFile ""}} {
    if {! [file exists $dstFolder]} {
      file mkdir $dstFolder
    }
    if {[string length $tgzFile] == 0} {
      set tgzFile [getUpload]
    }
    exec tar -zxvf $tgzFile -C $dstFolder
  }

  proc getUpload {{ptn ""}} {
    variable envdict
    set total [dict get $envdict software ftou]
    if {[string length $ptn] > 0} {
      foreach f $total {
        if {[string match $ptn $f]} {
          return $f
        }
      }
      return {}
    } else {
      if {[llength $total] > 0} {
        return [lindex $total 0]
      } else {
        return {}
      }
    }
  }

  proc getUploads {{ptn ""}} {
    variable envdict
    set total [dict get $envdict software ftou]
    if {[string length $ptn] > 0} {
      set files {}
      foreach f $total {
        if {[string match $ptn $f]} {
          lappend files $f
        }
      }
      return $files
    } else {
      return $total
    }
  }

  proc loadYaml {fileName} {
    if {[catch {set dt [::yaml::yaml2dict -file $fileName]} msg o]} {
      puts $msg
      exit 1
    } else {
      return $dt
    }
  }

  proc readEnvFileJson {fileName} {
    set jd [::json::json2dict [readWholeFile $fileName]]
    set bgConfigContent [dict get $jd boxGroup configContent]
    dict set jd boxGroup configContent [::json::json2dict $bgConfigContent]
    set sfConfigContent [dict get $jd software configContent]
    dict set jd software configContent [::json::json2dict $sfConfigContent]

    return $jd
  }

  proc readWholeFile {fileName} {
    if {[catch {open $fileName} fid o]} {
      puts $fid
      exit 1
    } else {
      set data [read $fid]
      close $fid
    }
    return $data
  }

  proc writeFile {fileName content} {
    if {[catch {open $fileName w} fid o]} {
      puts $fid
      exit 1
    } else {
      puts $fid $content
      close $fid
    }
  }

  proc writeFileLines {fileName lines} {
    if {[catch {open $fileName w} fid o]} {
      puts $fid
      exit 1
    } else {
      foreach line $lines {
          puts $fid $line
      }
      close $fid
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

  proc isInstalled {execName} {
    catch {exec which $execName} msg o
    if {[dict get $o -code] == 0} {
      return 1
    }
    return 0
  }
}
