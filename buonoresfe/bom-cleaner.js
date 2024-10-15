const fs = require("fs");
const os = require("os");

const start = () => {
  try {
    let text = fs.readFileSync("bom.csv", "utf8");
    // let separator = os.EOL;
    let separator = "\n";
    let lines = text.split(separator);

    lines = lines.map(el => {
      let parts = el.split(",");
      parts = parts.slice(0, 3);
      return parts.join(",");
    });

    text = lines.join(separator);
    fs.writeFileSync("bom.csv", text);
  } catch (err) {
    console.error(err);
  }
};

start();
