<#
tar (child): ./zookeeper-3.4.9.tar.gz1: Cannot open: No such file or directory
tar (child): Error is not recoverable: exiting now
/usr/bin/tar: Child returned status 2
/usr/bin/tar: Error is not recoverable: exiting now

zookeeper-3.4.9/zookeeper-3.4.9.jar.md5
/usr/bin/tar: zookeeper-3.4.9/zookeeper-3.4.9.jar.md5: Cannot open: File exists
/usr/bin/tar: zookeeper-3.4.9: Cannot utime: Operation not permitted
/usr/bin/tar: Exiting with failure status due to previous errors

https://blogs.technet.microsoft.com/heyscriptingguy/2014/03/30/understanding-streams-redirection-and-write-host-in-powershell/
#>
function Run-Tar {
 Param
     (
       [parameter(Position=0, Mandatory=$True)]
       [String]
       $TgzFileName,
       [parameter(Mandatory=$False)]
       [String]
       $DestFolder
    )
    if ($DestFolder) { # had destFolder parameter
        if (!(Test-Path $DestFolder)) { # if not exists.
#            if ((Get-Item $DestFolder).PSIsContainer) {
            New-Item $DestFolder -ItemType Directory | Out-Null
        }
    }
    $command = "tar -zxvf $TgzFileName $(if ($DestFolder) {`" -C $DestFolder`"} else {''}) *>&1"
    $r = $command | Invoke-Expression | Where-Object {$_ -cmatch "Cannot open:.*"} | measure
    if ($r.Count -gt 0) {$false} else {$True}
}