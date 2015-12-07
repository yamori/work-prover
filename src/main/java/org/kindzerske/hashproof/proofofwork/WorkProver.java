package org.kindzerske.hashproof.proofofwork;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.StringUtils;

public class WorkProver {

	MessageDigest messageDigest;
	private String challengeText;
	private int timeToSolveMS;
	private String successfulNonce;
	private String successfulHash;

	public static void main(String[] args) {

		WorkProver workProver = null;
		try {
			workProver = new WorkProver();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String tryString = "test this out for all mankind.";
		String successNonce = null;
		try {
			successNonce = workProver.solveChallenge(tryString, 5);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		System.out.println(tryString + " + " + successNonce);
		System.out.println(workProver.getSuccessfulHash());
		System.out.println(" Time to solve: " + workProver.getTimeToSolveMS() + "ms");
	}

	private byte[] toBytes(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /* >> 0 */);

		return result;
	}

	public WorkProver() throws NoSuchAlgorithmException {
		// Ready the MessageDiges
		this.messageDigest = MessageDigest.getInstance("SHA-256");
	}

	public String solveChallenge(String challengeText, int leadingZeros) throws UnsupportedEncodingException {
		this.challengeText = challengeText;
		String hashPrefixGoal = StringUtils.repeat("0", leadingZeros);

		// Measure the time to succeed
		long startTime = System.nanoTime();

		int nonceInteger = 0;
		String currentNonce = getHexNonceFromInteger(nonceInteger);
		String currentHash = hashSHA256(challengeText + currentNonce);
		while (!currentHash.substring(0, leadingZeros).equalsIgnoreCase(hashPrefixGoal)) {
			nonceInteger += 1;
			currentNonce = getHexNonceFromInteger(nonceInteger);
			currentHash = hashSHA256(challengeText + currentNonce);
		}

		long estimatedTime = System.nanoTime() - startTime;

		// Capture as instance vars to avoid being lost.
		this.successfulNonce = currentNonce;
		this.successfulHash = currentHash;
		this.timeToSolveMS = (int) Math.floor(estimatedTime / 1000000.0);

		return this.successfulNonce;
	}

	private String hashSHA256(String challengeText) throws UnsupportedEncodingException {
		this.messageDigest.update(challengeText.getBytes("UTF-8")); // Change
																	// this to
																	// "UTF-16"
																	// if needed
		byte[] digest = this.messageDigest.digest();
		return String.format("%064x", new java.math.BigInteger(1, digest));
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

	public String getSuccessfulHash() {
		return this.successfulHash;
	}

}
