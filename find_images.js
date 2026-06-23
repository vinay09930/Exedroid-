const fs = require('fs');
const path = require('path');
function walk(dir) {
  let results = [];
  try {
    let list = fs.readdirSync(dir);
    list.forEach(function(file) {
      file = path.resolve(dir, file);
      let stat = fs.statSync(file);
      if (stat && stat.isDirectory()) { 
        if (!file.includes('node_modules') && !file.includes('.gradle') && !file.includes('.build-outputs')) {
          results = results.concat(walk(file));
        }
      } else { 
        if(file.endsWith('.png') || file.endsWith('.jpg') || file.endsWith('.jpeg')) results.push(file);
      }
    });
  } catch (e) {}
  return results;
}
console.log(walk('/'));
