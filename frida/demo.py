import sys
import time
import frida

def on_message(message,data):
    print("message",message)
    print("data",data)

device = frida.get_usb_device()
session = device.attach("AndroidDemo")

with open("./hook.js","r") as f:
    script = session.create_script(f.read())

script.on("message",on_message)
script.load()
sys.stdin.read()