# Work-Prover

Class used for proof-of-work type computation using SHA256 hash to find a nonce which solves for n leading zeroes (base64).

## Motivation

Bitcoin uses proof-of-work to maintain the ledger, and proof-of-work has been used to deter DoS for a while.  This is small class to demonstrate how the brute-force works with nonces and SHA256.  

## Usage - main()

Fire up WorkProver.main() with no args to show usage.  Then modify the run configuration.  As an example:

'-s "test this text string" -n 4`

## Usage - jar

Run a maven install do generate an executable jar.  Execute with no args to see usage, or example  usage:

`java -jar WorkProver.jar -s "test this text string" -n 4`

## Warning

It's crude.  Have fun.  Share.