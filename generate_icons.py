#!/usr/bin/env python3
from PIL import Image
import os

# Define the icon specifications
icons = [
    ("mipmap-mdpi", 48),
    ("mipmap-hdpi", 72),
    ("mipmap-xhdpi", 96),
    ("mipmap-xxhdpi", 144),
    ("mipmap-xxxhdpi", 192),
]

base_path = r"app\src\main\res"

# Create directories and generate PNG files
for mipmap_dir, size in icons:
    dir_path = os.path.join(base_path, mipmap_dir)
    os.makedirs(dir_path, exist_ok=True)
    
    # Create ic_launcher.png (solid blue)
    img_launcher = Image.new('RGBA', (size, size), color=(0, 102, 204, 255))
    launcher_path = os.path.join(dir_path, "ic_launcher.png")
    img_launcher.save(launcher_path, 'PNG')
    print(f"Created: {launcher_path}")
    
    # Create ic_launcher_round.png (solid blue)
    img_launcher_round = Image.new('RGBA', (size, size), color=(0, 102, 204, 255))
    launcher_round_path = os.path.join(dir_path, "ic_launcher_round.png")
    img_launcher_round.save(launcher_round_path, 'PNG')
    print(f"Created: {launcher_round_path}")

print("\nAll icon files created successfully!")
