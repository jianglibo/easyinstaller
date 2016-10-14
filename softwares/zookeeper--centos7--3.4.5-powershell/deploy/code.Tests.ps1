$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$sut = (Split-Path -Leaf $MyInvocation.MyCommand.Path) -replace '\.Tests\.', '.'
. "$here\$sut" -envfile (Join-Path -Path (Split-Path -Path $here -Parent) -ChildPath test\envforcodeexec.xml)

function Get-NewPix
  {
#     $start = Get-Date -Month 1 -Day 1 -Year 2010
#     $allpix = Get-ChildItem -Path $env:UserProfile\*.jpg -Recurse
#     $allpix | where {$_.LastWriteTime -gt $Start}
    1,2,3
}   

          function Get-SmallFiles {
              param ($size = 100) # default is 100
              Get-ChildItem c:\ | where {$_.Length -lt $Size -and !$_.PSIsContainer} 
          }

            function Get-SmallFiles {
              param (
              [PSDefaultValue(Help = '100')]
              $size = 100
              )
              }
        # positional parameter
           function Get-Extension {
              $name = $args[0] + ".txt"
              $name
          }

          # switch parameter
          function Switch-Item {
              param ([switch]$on)
              if ($on) { "Switch on" }
              else { "Switch off" }
          }
Describe "code" {
    It "should all be false" {
        $(if ("") {"space"} else {"True"}) | Should Be "True"
        0 -eq $false | Should Be $True
        -not 0 | Should Be $True
        -not 1 | Should Be $False
        $true -eq 2 | Should Be $True
        2 -eq $true | Should Be $False
        -not "" | Should Be $True
        "" -eq $false | Should Be $False
        $false -eq "" | Should Be $True

    }
    It "envForExec not be empty." {
        $envForExec | Should Be $True
    }
<#
    It "should be XmlElement" {
        $envForExec.boxGroup.boxes | select ip | Get-Member | select -ExpandProperty TypeName|  Should Be "Selected.System.Xml.XmlElement"
    }
    It "should be HashTable" {
        $envForExec.boxGroup.boxes | foreach {@{ip=$_.ip; hostname=$_.hostname}} | Get-Member | select -ExpandProperty TypeName|  Should Be "System.Collections.Hashtable"
    }
    It "should still be XmlElement" {
        $envForExec.boxGroup.boxes | Select-Object ip, hostname, @{name="serverId"; expression={$_.ip.split(".")[-1]}} | Get-Member | select -ExpandProperty TypeName|  Should Be "Selected.System.Xml.XmlElement"
    }
    It "should get scalar value" {
        $envForExec.boxGroup.boxes | Select-Object ip, hostname, @{name="serverId"; expression={$_.ip.split(".")[-1]}} | Select-Object -First 1 -ExpandProperty serverId | Should Be "14"
    }
#>
    It "should format string" {
        $ht = @{str="abc"; int=55}
        "$($ht.str)xxx" | Should Be "abcxxx"
    }

    It "should create custom object" {
        $obj = [PSCustomObject]@{
            Property1 = 'one'
            Property2 = 'two'
            Property3 = 'three'
        }

        $obj | Get-Member | select -ExpandProperty TypeName|  Should Be "System.Management.Automation.PSCustomObject"

        $obj = New-Object PSObject
        Add-Member -InputObject $obj -MemberType NoteProperty -Name customproperty -Value ""

        $obj | Get-Member | select -ExpandProperty TypeName|  Should Be "System.Management.Automation.PSCustomObject"
    }

    It "should has custom type for custom object" {
        $obj = "" | select prop1, prop2
        $obj.pstypenames.insert(0,'Custom.ObjectExample')

        $obj | get-member | select -ExpandProperty TypeName|  Should Be "Custom.ObjectExample"
    }

    It "is an alias of foreach %" {
        ((Get-Alias -Definition ForEach-Object) | Where-Object {$_.Name -Match "^%.*"}  | Measure-Object).Count | Should Be 1
    }

    It "is all psdriver" {
        (Get-PSDrive | measure).Count -gt 0 | Should Be $True
    }

    It "is a simple function" {
        (Get-NewPix | select -First 2 | measure).Count -gt 0 | Should Be $True
    }

    It "should be configCongtent object." {
        $softwareConfig.zkports | Should Be "2888,3888"
    }

}

# https://technet.microsoft.com/en-us/library/hh847829.aspx