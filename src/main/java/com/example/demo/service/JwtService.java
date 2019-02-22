package com.example.demo.service;

import java.util.Date;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtService {
	public static final String USERNAME = "username";
	public static final String SECRET_KEY = "11111111111111111111111111111111";
	public static final int EXPIRE_TIME = 86400000;

	public String generateTokenLogin(String username) {
		String token = null;
		try {
			// Create HMAC signer
			JWSSigner signer = new MACSigner(generateShareSecret());
			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.claim(USERNAME, username);
			builder.expirationTime(generateExpirationDate());
			JWTClaimsSet claimsSet = builder.build();
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
			// Apply the HMAC protection
			signedJWT.sign(signer);
			// Serialize to compact form, produces something like
			// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
			token = signedJWT.serialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + EXPIRE_TIME);
	}

	private Date getExpirationDateFromToken(String token) {
		Date expiration = null;
		JWTClaimsSet claims = getClaimsFromToken(token);
		expiration = claims.getExpirationTime();
		return expiration;
	}

	private Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private byte[] generateShareSecret() {
		// Generate 256-bit (32-byte) shared secret
		byte[] sharedSecret = new byte[32];//5
		sharedSecret = SECRET_KEY.getBytes();//6 auto get key
		return sharedSecret;//7
	}
	
	private JWTClaimsSet getClaimsFromToken(String token) {
		JWTClaimsSet claims = null;
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);//4
			JWSVerifier verifier = new MACVerifier(generateShareSecret());//8
			if (signedJWT.verify(verifier)) { //verified = verifier.verify(getHeader(), getSigningInput(), getSignature()); //9
				claims = signedJWT.getJWTClaimsSet();//10
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claims;//11
	}

	public String getUsernameFromToken(String token) {
		String username = null;
		try {
			JWTClaimsSet claims = getClaimsFromToken(token);//3
			username = claims.getStringClaim(USERNAME);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return username;//12
	}
	
	public Boolean validateTokenLogin(String token) {
		if (token == null || token.trim().length() == 0) {
			return false;//1
		}
		String username = getUsernameFromToken(token);//2
		if (username == null || username.isEmpty()) {
			return false;//13
		}
		if (isTokenExpired(token)) {
			return false;//14
		}
		return true;//15 true -->return 
	}
	
}