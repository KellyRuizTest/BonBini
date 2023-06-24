package com.twenty.bonbini.Model

class Users {

    private var name : String = ""
    private var email : String = ""
    private var passowrd: String = ""
    private var pid : String = ""
    private var telefono : String = ""

    constructor()

    constructor(name: String, email: String, passowrd: String, pid: String, telefono: String) {
        this.name = name
        this.email = email
        this.passowrd = passowrd
        this.pid = pid
        this.telefono = telefono
    }

    fun getName() : String {return name}
    fun getEmail() : String{return email}
    fun getPassword() : String{ return passowrd}
    fun getPid() : String{return pid}
    fun getTelefono() : String{return telefono}


    fun setName(foo : String) {name = foo}
    fun setEmail(foo : String) {email = foo}
    fun setPassword(foo : String) {passowrd = foo}
    fun setTelefono(foo : String) {telefono = foo}

}