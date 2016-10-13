$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$sut = (Split-Path -Leaf $MyInvocation.MyCommand.Path) -replace '\.Tests\.', '.'
. "$here\$sut"

[xml]$clusterCfg = Get-Content (Join-Path -Path (Split-Path -Path $here -Parent) -ChildPath test\envforcodeexec.xml)

Describe "code" {
    It "should be XmlElement" {
        $clusterCfg.EnvForCodeExec.boxGroup.boxes | select ip | Get-Member | select -ExpandProperty TypeName|  Should Be "Selected.System.Xml.XmlElement"
    }
    It "should be HashTable" {
        $clusterCfg.EnvForCodeExec.boxGroup.boxes | foreach {@{ip=$_.ip; hostname=$_.hostname}} | Get-Member | select -ExpandProperty TypeName|  Should Be "System.Collections.Hashtable"
    }
    It "should still be XmlElement" {
        $clusterCfg.EnvForCodeExec.boxGroup.boxes | Select-Object ip, hostname, @{name="serverId"; expression={$_.ip.split(".")[-1]}} | Get-Member | select -ExpandProperty TypeName|  Should Be "Selected.System.Xml.XmlElement"
    }
    It "should get scalar value" {
        $clusterCfg.EnvForCodeExec.boxGroup.boxes | Select-Object ip, hostname, @{name="serverId"; expression={$_.ip.split(".")[-1]}} | Select-Object -First 1 -ExpandProperty serverId | Should Be "10"
    }
    It "should format string" {
        $ht = @{str="abc"; int=55}
        "$($ht.str)xxx" | Should Be "abcxxx"
    }

}
