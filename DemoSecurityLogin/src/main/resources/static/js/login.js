var login = (function () {

    function login(){
        let uname = document.getElementById("username").value;
        let passwd = document.getElementById("passwd").value;
        let user = {username : uname, password:passwd};
        axios.post("/login",user).then(res=>{
            if(res.data!==""){
                alert("Invalid username or password");
            }
            else {
                window.location.href="protected/test.html";
            }
        })
    }

    function logout(){
        axios.get("/logout").then(res=>{
            alert("You have closed your session");
            window.location.href = "/index.html" ;
        })
    }

    return {
        login:login,
        logout:logout
    };
})();