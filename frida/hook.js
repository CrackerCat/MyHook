function hook_Activity() {
    Java.perform(function () {
        var LoginActivity = Java.use("com.github.lastingyang.androiddemo.Activity.LoginActivity");
        LoginActivity.a.overload('java.lang.String', 'java.lang.String').implementation = function (str, str2) {
            var result = this.a(str, str2); //调用被hook前的函数
            console.log("LoginActivity.a:", str, str2, result);
            return result;
        }
    console.log("hook_Activity");
    });
}