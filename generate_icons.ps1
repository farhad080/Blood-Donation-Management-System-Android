# Function to create a simple PNG file
function Create-SimplePNG {
    param(
        [string]$FilePath,
        [int]$Width,
        [int]$Height,
        [byte]$R = 0,
        [byte]$G = 102,
        [byte]$B = 204
    )
    
    # Ensure directory exists
    $dir = Split-Path -Parent $FilePath
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }
    
    # Create a bitmap using .NET
    Add-Type -AssemblyName System.Drawing
    
    $bitmap = New-Object System.Drawing.Bitmap($Width, $Height)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    # Fill with solid color (blue)
    $color = [System.Drawing.Color]::FromArgb(255, $R, $G, $B)
    $brush = New-Object System.Drawing.SolidBrush($color)
    $graphics.FillRectangle($brush, 0, 0, $Width, $Height)
    
    # Clean up graphics
    $graphics.Dispose()
    $brush.Dispose()
    
    # Save as PNG
    $bitmap.Save($FilePath, [System.Drawing.Imaging.ImageFormat]::Png)
    $bitmap.Dispose()
    
    Write-Host "Created: $FilePath"
}

# Define icon specifications
$icons = @(
    @{ MipmapDir = "mipmap-mdpi"; Size = 48 },
    @{ MipmapDir = "mipmap-hdpi"; Size = 72 },
    @{ MipmapDir = "mipmap-xhdpi"; Size = 96 },
    @{ MipmapDir = "mipmap-xxhdpi"; Size = 144 },
    @{ MipmapDir = "mipmap-xxxhdpi"; Size = 192 }
)

$basePath = "app\src\main\res"

# Generate all icon files
foreach ($icon in $icons) {
    $dirPath = Join-Path $basePath $icon.MipmapDir
    
    # Create ic_launcher.png
    $launcherPath = Join-Path $dirPath "ic_launcher.png"
    Create-SimplePNG -FilePath $launcherPath -Width $icon.Size -Height $icon.Size
    
    # Create ic_launcher_round.png
    $roundPath = Join-Path $dirPath "ic_launcher_round.png"
    Create-SimplePNG -FilePath $roundPath -Width $icon.Size -Height $icon.Size
}

Write-Host "`nAll icon files created successfully!"
