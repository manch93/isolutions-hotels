<#
Updates the Android launcher icons (ic_launcher.png and ic_launcher_round.png) in the project
from a single source PNG using ImageMagick (magick CLI).

Usage (PowerShell):
  .\scripts\update-launcher-icon.ps1 -Source "C:\Users\Manch\Downloads\ihotels_logo.png"

Requirements:
 - ImageMagick installed and the `magick` command available on PATH.
 - Run this script from the repository (or it will locate the repo root relative to the script).

What it does:
 - Creates backups of existing mipmap icon files (adds .bak timestamp)
 - Generates resized PNGs for mdpi/hdpi/xhdpi/xxhdpi/xxxhdpi
 - Writes both ic_launcher.png and ic_launcher_round.png in each folder
 - Does NOT create files in mipmap-anydpi-v26 (to avoid xml/png conflicts)
#>
param(
    [Parameter(Mandatory=$true)]
    [string]$Source
)

if (-not (Test-Path $Source)) {
    Write-Error "Source file not found: $Source"
    exit 2
}

# check magick
$magick = Get-Command magick -ErrorAction SilentlyContinue
if (-not $magick) {
    Write-Error "ImageMagick 'magick' not found on PATH. Install ImageMagick and ensure 'magick' is on PATH."
    exit 3
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$repoRoot = Resolve-Path (Join-Path $scriptDir "..")
$projectRes = Join-Path $repoRoot "app\src\main\res"

if (-not (Test-Path $projectRes)) {
    Write-Error "Could not locate app resources folder at: $projectRes"
    exit 4
}

# densities and sizes (px)
$map = @{
    "mdpi" = 48
    "hdpi" = 72
    "xhdpi" = 96
    "xxhdpi" = 144
    "xxxhdpi" = 192
}

# Avoid writing into mipmap-anydpi-v26 to prevent collisions with adaptive xml files
foreach ($d in $map.GetEnumerator()) {
    $folder = Join-Path $projectRes ("mipmap-{0}" -f $d.Key)
    if (-not (Test-Path $folder)) {
        Write-Host "Creating folder $folder"
        New-Item -ItemType Directory -Path $folder | Out-Null
    }

    $size = $d.Value
    $destLauncher = Join-Path $folder "ic_launcher.png"
    $destRound = Join-Path $folder "ic_launcher_round.png"

    # backup existing
    foreach ($f in @($destLauncher, $destRound)) {
        if (Test-Path $f) {
            $ts = Get-Date -Format "yyyyMMddHHmmss"
            $bak = "$f.bak.$ts"
            Write-Host "Backing up $f -> $bak"
            Move-Item -Path $f -Destination $bak -Force
        }
    }

    # create resized icon and round icon (same content; Android will mask round when needed)
    Write-Host "Generating $size x $size -> $destLauncher"
    magick "$Source" -resize ${size}x${size} "$destLauncher"

    Write-Host "Generating $size x $size -> $destRound"
    magick "$Source" -resize ${size}x${size} "$destRound"
}

# Remove accidental pngs from mipmap-anydpi-v26 that would conflict with xml (if any exist)
$anydpi = Join-Path $projectRes "mipmap-anydpi-v26"
if (Test-Path $anydpi) {
    foreach ($name in @("ic_launcher.png","ic_launcher_round.png","ic_launcher.xml","ic_launcher_round.xml")) {
        $p = Join-Path $anydpi $name
        if ((Test-Path $p) -and ($p -like "*.png")) {
            $ts = Get-Date -Format "yyyyMMddHHmmss"
            $bak = "$p.bak.$ts"
            Write-Host "Found $p in mipmap-anydpi-v26 (may conflict with adaptive xml). Backing up -> $bak and removing original."
            Move-Item -Path $p -Destination $bak -Force
        }
    }
}

Write-Host "Launcher icon update complete. Verify adaptive icon XML (mipmap-anydpi-v26/ic_launcher.xml) points to the correct foreground/background drawables if needed."
Write-Host "Remember to build the project (Gradle) after replacing icons. If you get a JDK error, upgrade Java to 17+ before building."
