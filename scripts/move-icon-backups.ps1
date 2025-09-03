$root='C:\Users\Manch\GitlabProjects\isolutions-hotels\app\src\main\res'
$ts=Get-Date -Format 'yyyyMMddHHmmss'
$destRoot=Join-Path 'C:\Users\Manch\GitlabProjects\isolutions-hotels\scripts\icon-backups' $ts
New-Item -ItemType Directory -Path $destRoot -Force | Out-Null
$files = Get-ChildItem -Path $root -Recurse -Filter '*.bak.*'
foreach ($f in $files) {
    $full=$f.FullName
    $rel=$full.Substring($root.Length+1)
    $dest=Join-Path $destRoot $rel
    New-Item -ItemType Directory -Path (Split-Path $dest) -Force | Out-Null
    Move-Item -Path $full -Destination $dest -Force
    Write-Host "Moved: $full -> $dest"
}
Write-Host 'Move complete'