# Work-Prover

Java class used for proof-of-work type computation using SHA256 hash to find a nonce which solves for `n` leading zeroes (base64).

## Example Output

`$ java -jar target/WorkProver-jar-with-dependencies.jar -s "test this text string" -n 4'

'*** WorkProver: clearText + nonce = SHA256 hash with leading zeroes.  Found brute forced.'

'Clear text and desired leading zeroes (repsectively):'

'test this text string'

'4'

'Proceed? (y/n): y'

'test this text string + 9285'

'00007f84285bfce4cd8be238507b1aa2029bae4a4c878ab0a95d5eaf200b7913'

' Time to solve: 677ms (3.7509E4 nonces)`

## Motivation

Bitcoin uses proof-of-work to maintain the ledger, and proof-of-work has been used to deter DoS for a while.  This is a small class to demonstrate how the brute-force works with nonces and SHA256.  

## Usage - main()

Import the project in Eclipse and fire up WorkProver.main() with no args to show usage.  Then modify the run configuration.  As an example:

`-s "test this text string" -n 4`

## Usage - jar

Run a maven install to generate an executable jar.  Execute with no args to see usage, or as an example:

`java -jar WorkProver.jar -s "test this text string" -n 4`

## Warning

It's crude.  Have fun.  Share.