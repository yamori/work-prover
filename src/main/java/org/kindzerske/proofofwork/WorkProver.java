package org.kindzerske.proofofwork;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

/**
 * Class used for proof-of-work type computation using SHA256 hash to find a
 * nonce which solves for n leading zeroes (base64).
 * 
 * @author matthew.kindzerske
 *
 */
public class WorkProver {

	MessageDigest messageDigest;
	private String challengeText;
	private int timeToSolveMS;
	private String successfulNonce;
	private int successfulNonceInt;
	private String successfulHash;

	public static void main(String[] args) {

		System.out.println("*** WorkProver: clearText + nonce = SHA256 hash with leading zeroes.  Found brute forced.");

		// Options object for CLI.
		Options options = new Options();
		options.addOption("s", true, "clear string");
		options.addOption("n", true,
				"number of leading zeros for solution, 1..64, but careful with anything n>3 ... or have patience");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		String clearText = "";
		int leadingZeroes = 1; // Default
		if (cmd.hasOption("s") && cmd.hasOption("n")) {
			clearText = cmd.getOptionValue("s");
			leadingZeroes = Integer.parseInt(cmd.getOptionValue("n"));
		} else {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar WorkProver.jar", options);
			// Parameters not correctly determined, terminate.
			return;
		}

		System.out.println("Clear text and desired leading zeroes (respectively): ");
		System.out.println(clearText);
		System.out.println(leadingZeroes);
		System.out.print("Proceed? (y/n): ");

		Scanner sc = new Scanner(System.in);
		String scannerInput = sc.next();
		sc.close();
		if (!scannerInput.equalsIgnoreCase("y")) {
			System.out.println("terminated.");
			return;
		} else {
			// Continue processing
		}

		WorkProver workProver = null;
		try {
			workProver = new WorkProver();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String successNonce = null;
		try {
			successNonce = workProver.solveChallenge(clearText, leadingZeroes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat("0.#####E0");

		System.out.println("\"" + clearText + "\" + \"" + successNonce + "\"");
		System.out.println(workProver.getSuccessfulHash());
		System.out.println(" Time to solve: " + workProver.getTimeToSolveMS() + "ms ("
				+ formatter.format(workProver.getSuccessfulNonceInt()) + " nonces)");
	}

	/**
	 * Class used proof-of-work type computation using SHA256 hash to find a
	 * nonce which solves for n leading zeroes (base64).
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public WorkProver() throws NoSuchAlgorithmException {
		// Ready the MessageDigest
		this.messageDigest = MessageDigest.getInstance("SHA-256");
	}

	/**
	 * This method will take the challenge text, and brute force find an
	 * appended nonce which will solve a SHA256 hash resulting in n leading
	 * zeros (base 64).
	 * 
	 * @param challengeText
	 *            String. Text used in the challenge.
	 * @param leadingZeros
	 *            Integer. Number of desired leading zeros (base 64). The larger
	 *            the number, the more complex the task. Be careful.
	 * @return String The nonce used to solve the problem.
	 * @throws UnsupportedEncodingException
	 */
	public String solveChallenge(String challengeText, int leadingZeros) throws UnsupportedEncodingException {
		this.challengeText = challengeText;
		String hashPrefixGoal = StringUtils.repeat("0", leadingZeros);

		// Measure the time to succeed
		long startTime = System.nanoTime();
		NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat("0.#####E0");

		int nonceInteger = 0;
		String currentNonce = getHexNonceFromInteger(nonceInteger);
		String currentHash = hashSHA256(challengeText + currentNonce);
		while (!currentHash.substring(0, leadingZeros).equalsIgnoreCase(hashPrefixGoal)) {
			nonceInteger += 1;
			currentNonce = getHexNonceFromInteger(nonceInteger);
			currentHash = hashSHA256(challengeText + currentNonce);

			if (nonceInteger % 500000 == 0) {
				// For seeing status
				System.out.println(" # of nonces tried: " + formatter.format(nonceInteger));
			}
		}

		long estimatedTime = System.nanoTime() - startTime;

		// Capture as instance vars to avoid being lost.
		this.successfulNonce = currentNonce;
		this.successfulNonceInt = nonceInteger;
		this.successfulHash = currentHash;
		this.timeToSolveMS = (int) Math.floor(estimatedTime / 1000000.0);

		return this.successfulNonce;
	}

	/**
	 * Method used to hash clearText using SHA256. Can be used to verify output
	 * from solveChallenge() since this same method is used.
	 * 
	 * @param clearText
	 *            String. Text to be hashed.
	 * @return String. 64 chars of SHA256 applied to clearText
	 * @throws UnsupportedEncodingException
	 */
	public String hashSHA256(String clearText) throws UnsupportedEncodingException {
		this.messageDigest.update(clearText.getBytes("UTF-8")); // Change
																// this to
																// "UTF-16"
																// if needed
		byte[] digest = this.messageDigest.digest();
		return String.format("%064x", new java.math.BigInteger(1, digest));
	}
	
	private byte[] toBytes(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /* >> 0 */);

		return result;
	}

	private String getHexNonceFromInteger(int nonceInt) {
		return String.format("%x", new java.math.BigInteger(1, toBytes(nonceInt)));
	}

	public String getChallengeText() {
		return this.challengeText;
	}

	public int getTimeToSolveMS() {
		return this.timeToSolveMS;
	}

	public String getSuccessfulNonce() {
		return this.successfulNonce;
	}

	public int getSuccessfulNonceInt() {
		return successfulNonceInt;
	}

	public String getSuccessfulHash() {
		return this.successfulHash;
	}

}
