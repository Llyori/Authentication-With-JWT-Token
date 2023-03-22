package com.Travel.Hot.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUser;
	private String email;
	private String nom;
	private String prenom;
	private String numeroCNI;
	private String numeroTelephone;
	private String password;
	private String Adresse;
	@ManyToOne
	@JoinColumn(name = "idRole")
	Role roles;
	
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNumeroCNI() {
		return numeroCNI;
	}
	public void setNumeroCNI(String numeroCNI) {
		this.numeroCNI = numeroCNI;
	}
	public String getNumeroTelephone() {
		return numeroTelephone;
	}
	public void setNumeroTelephone(String numeroTelephone) {
		this.numeroTelephone = numeroTelephone;
	}
	public String getAdresse() {
		return Adresse;
	}
	public void setAdresse(String adresse) {
		Adresse = adresse;
	}
	
	public Role getRoles() {
		return roles;
	}
	public void setRoles(Role roles) {
		this.roles = roles;
	}
	
	public User() {
		
	}
	
	public User(String email, String nom, String prenom, String numeroCNI, String numeroTelephone,
			String password, String adresse, Role roles) {
		super();
		this.email = email;
		this.nom = nom;
		this.prenom = prenom;
		this.numeroCNI = numeroCNI;
		this.numeroTelephone = numeroTelephone;
		this.password = password;
		Adresse = adresse;
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", email=" + email + ", nom=" + nom + ", prenom=" + prenom + ", numeroCNI="
				+ numeroCNI + ", numeroTelephone=" + numeroTelephone + ", Adresse=" + Adresse + ", roles=" + roles
				+ "]";
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
