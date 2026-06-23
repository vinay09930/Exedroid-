const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const sourceImage = process.argv[2];
if (!sourceImage || !fs.existsSync(sourceImage)) {
    console.error("Please provide a valid source image path");
    process.exit(1);
}

const resDir = path.join(__dirname, 'app', 'src', 'main', 'res');

const sizes = {
    'mipmap-mdpi': 48,
    'mipmap-hdpi': 72,
    'mipmap-xhdpi': 96,
    'mipmap-xxhdpi': 144,
    'mipmap-xxxhdpi': 192
};

console.log("Installing sharp-cli...");
execSync('npm install -g sharp-cli', { stdio: 'inherit' });

for (const [dir, size] of Object.entries(sizes)) {
    const outDir = path.join(resDir, dir);
    if (!fs.existsSync(outDir)) {
        fs.mkdirSync(outDir, { recursive: true });
    }
    const outFile = path.join(outDir, 'ic_launcher.png');
    console.log(`Generating ${dir} (${size}x${size})...`);
    execSync(`npx sharp -i "${sourceImage}" -o "${outFile}" resize ${size} ${size}`, { stdio: 'inherit' });
}

const playStoreIcon = path.join(__dirname, 'app', 'src', 'main', 'play_store_512.png');
console.log(`Generating Play Store icon (512x512)...`);
execSync(`npx sharp -i "${sourceImage}" -o "${playStoreIcon}" resize 512 512`, { stdio: 'inherit' });

console.log("Icon generation complete!");
