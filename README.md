## About
This is a CMD typing exercise inspired by [Monkeytype](https://monkeytype.com/).

## Usage
| Option | Description |
|--------|-------------|
| `-f`, `--file <file-path>` | Load words from a file. |
| `-h`, `--help` | Show the help message. |
| `-n <number>` | Specify the number of words. |
| `-p`, `--pattern <"regex">` | Get words matching the specified regular expression pattern. |

## Building
Build with the command \
`./gradlew clean shadowJar` \
\
Requires Java 21 or above.

## Running
`java -jar build/libs/penguintype-1.0.0-all.jar`\
\
Run with the `-h` flag to see options.

## Releases
Running releases does not require Java as they are bundled with JRE.\
Unzip and run the executable inside the `bin` directory.