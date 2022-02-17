// 打印参数 
function hook_LoginActivity() 
{
    Java.perform(function() 
    {
        var LoginActivity = Java.use("com.example.androiddemo.Activity.LoginActivity");
        LoginActivity.a.overload('java.lang.String', 'java.lang.String').implementation = function (str, str2) 
        {
            var result = this.a(str, str2);
            console.log("LoginActivity.a:", str, str2, result);
            return "admin";
        };
    });
    console.log("LoginActivity Hook~~")
}

// 修改返回
function hook_FridaActivity1()
{
    Java.perform(function()
    {
        var FridaActivity1 = Java.use("com.example.androiddemo.Activity.FridaActivity1");
        FridaActivity1.a.implementation = function (barr) 
        {
            return "R4jSLLLLLLLLLLOrLE7/5B+Z6fsl65yj6BgC6YWz66gO6g2t65Pk6a+P65NK44NNROl0wNOLLLL=";
        };
    });
    console.log("FridaActivity1 Hook~~")
}

// 主动调用
function hook_FridaActivity2() 
{
    Java.perform(function() 
    {
        // 调用 静态函数
        var FridaActivity2 = Java.use("com.example.androiddemo.Activity.FridaActivity2");
        FridaActivity2.setStatic_bool_var();

        // 调用 非静态函数
        Java.choose("com.example.androiddemo.Activity.FridaActivity2", 
        {
            onMatch: function (instance) 
            {  
                instance.setBool_var(); 
            },

            onComplete: function () 
            {

            }
        });
    });
    console.log("FridaActivity2 Hook~~~")
}

// 修改成员变量
function hook_FridaActivity3() 
{
    Java.perform(function () 
    {
        // 设置 静态成员变量
        var FridaActivity3 = Java.use("com.example.androiddemo.Activity.FridaActivity3");
        FridaActivity3.static_bool_var.value = true;        
        console.log(FridaActivity3.static_bool_var.value);

        // 设置 非静态成员变量
        Java.choose("com.example.androiddemo.Activity.FridaActivity3", 
        {
            onMatch: function (instance) 
            {
                instance.bool_var.value = true;             // 非静态成员变量
                instance._same_name_bool_var.value = true;  // 有相同函数名的成员变量的值 （前面加下划线）
                console.log(instance.bool_var.value, instance._same_name_bool_var.value);
            },

            onComplete: function () 
            {

            }
        });
    });
}

// Hook内部类
function hook_FridaActivity4() 
{
    Java.perform(function () 
    {
        var InnerClasses = Java.use("com.example.androiddemo.Activity.FridaActivity4$InnerClasses");
        InnerClasses.check1.implementation = function() 
        {
            return true;
        };
        
        InnerClasses.check2.implementation = function()
        {
            return true;
        };
        
        InnerClasses.check3.implementation = function()
        {
            return true;
        };
        
        InnerClasses.check4.implementation = function()
        {
            return true;
        };
        
        InnerClasses.check5.implementation = function()
        {
            return true;
        };
        
        InnerClasses.check6.implementation = function()
        {
            return true;
        };
    });
    console.log("FridaActivity4$InnerClasses Hook~~~");
}


function hook_dyn_dex() 
{
    Java.perform(function() 
    {
        var FridaActivity5 = Java.use("com.example.androiddemo.Activity.FridaActivity5");
        Java.choose("com.example.androiddemo.Activity.FridaActivity5", 
        {
            onMatch: function (instance) 
            {
                console.log(instance.getDynamicDexCheck().$className);
            }, 
            
            onComplete: function () 
            {

            }
        });


        //hook 动态加载的dex
        Java.enumerateClassLoaders(
        {
            onMatch: function (loader) 
            {
                try 
                {
                    if (loader.findClass("com.example.androiddemo.Dynamic.DynamicCheck")) 
                    {
                        console.log(loader);
                        Java.classFactory.loader = loader;      // 切换 classloader
                    }
                } 
                catch (error) 
                {

                }
            }, 
            
            onComplete: function () 
            {

            }
        });

        var DynamicCheck = Java.use("com.example.androiddemo.Dynamic.DynamicCheck");
        console.log(DynamicCheck);
        
        DynamicCheck.check.implementation = function () 
        {
            console.log("DynamicCheck.check");
            return true;
        }
    });
}

function hook_FridaActivity6() 
{
    Java.perform(function () 
    {
        var Frida6Class0 = Java.use("com.example.androiddemo.Activity.Frida6.Frida6Class0");
        Frida6Class0.check.implementation = function () 
        {
            return true;
        };

        var Frida6Class1 = Java.use("com.example.androiddemo.Activity.Frida6.Frida6Class1");
        Frida6Class1.check.implementation = function () 
        {
            return true;
        };

        var Frida6Class2 = Java.use("com.example.androiddemo.Activity.Frida6.Frida6Class2");
        Frida6Class2.check.implementation = function () 
        {
            return true;
        };
    });
}

function hook_mul_class() 
{
    Java.perform(function () 
    {
        Java.enumerateLoadedClasses(
        {
            onMatch: function (name, handle) 
            {
                if (name.indexOf("com.example.androiddemo.Activity.Frida6") >= 0) 
                {
                    console.log(name);
                    var fridaclass6 = Java.use(name);

                    fridaclass6.check.implementation = function () 
                    {
                        console.log("frida 6 check:", this);
                        return true;
                    };
                }
            }, 
            
            onComplete: function () 
            {

            }
        })
    });
}

function main() 
{
    // hook_LoginActivity();
    // hook_FridaActivity1();
    // hook_FridaActivity2();
    // hook_FridaActivity3();
    hook_FridaActivity4();
    // hook_FridaActivity6();
}

setImmediate(main);

