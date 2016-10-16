$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$sut = (Split-Path -Leaf $MyInvocation.MyCommand.Path) -replace '\.Tests\.', '.'
. "$here\$sut"

Describe "ooo" {
    It "does something useful" {
        $true | Should Be $false
    }
}

# PSBASE, PSADAPTED,PSEXTENDED,PSOBJECT

<#
There are lots of different object & data technologies in the world, each with their own particulars.  Most of us never care about those particulars, we want the data and functions and that is that.  The particulars get in the way of our problem solving.  The clearest example of this is XML.  Just try to get your data out of XML – it’s a nightmere. 
 
With that as a backdrop, PowerShell “adapts” various object technologies to provide a standardized object view of them.  Another way to think of it is that we project an normalized Object VIEW the same way that a database projects a VIEW of various data tables (there are good reason’s why those tables exist they way they exist but as a user – they are not want I want so the DBA creates a VIEW).
 
So then what happens if the particular problem you are solving actually needs the particulars of the underlying technology?  That is where PSBASE comes in, it gives you  RAW access to the object itself.
 
We actually provide a number of VIEWS of the object:
#>