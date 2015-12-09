# Work-Prover

Java class used for proof-of-work type computation using SHA256 hash to find a nonce which solves for `n` leading zeroes (base64).

## Example

`$ java -jar target/WorkProver-jar-with-dependencies.jar -s "testing string" -n 4`

`"testing string" + "774c"`

`00002f1e274f49d8dd2267da49f39d942215f0f9c430ee074c3a20b8b11ec775`

`  Time to solve: 664ms (3.054E4 nonces)`

## Motivation

Bitcoin uses proof-of-work to maintain the ledger, and proof-of-work has been used to deter DoS for a while.  This is a small class to demonstrate how the brute-force works with nonces and SHA256.  

## Usage

### Eclipse - main()

Import the project in Eclipse and fire up WorkProver.main() with no args to show usage.  Then modify the run configuration.  As an example:

`-s "testing string" -n 4`

### Executable .jar

Run a maven install to generate an executable jar.  Execute with no args to see usage, or as an example:

`java -jar WorkProver.jar -s "testing string" -n 4`

## Sample Output

`$ java -jar target/WorkProver-jar-with-dependencies.jar -s "testing string" -n 4`

`*** WorkProver: clearText + nonce = SHA256 hash with leading zeroes.  Found brute forced.`

`Clear text and desired leading zeroes (repsectively):`

`testing string`

`4`

`Proceed? (y/n): y`

`"testing string" + "774c"`

`00002f1e274f49d8dd2267da49f39d942215f0f9c430ee074c3a20b8b11ec775`

` Time to solve: 664ms (3.054E4 nonces)`

## Warning

It's crude.  Have fun.  Share.