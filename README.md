# VolvoCANBUS
JAVA project to interact with a P2 Volvo S60 Canbus

An adaptation/interface onto can-utils (https://github.com/linux-can/can-utils)

The reason I started this version is because of the DPF (soot filter) and regeneration issues. The goal is to provide the driver with a simple feedback whenever the car is regenerating its DPF. The driver can then make the decision to keep on driving and let the process finish, or to cut the process short and run the risk of eventually getting messages such as:
"Emissions problem. Service required"
"DPF full. Regeneration required"

See also my other repository where I am basing this off of an Arduino board.

The changes in this version are specific to my Volvo S60 (MY2009), aka P2 facelift model.

You are welcome to use and expand on this software ON YOUR OWN RISK!

Connecting to the HS-CAN means you are possibly interfering with "under the bonnet" components (ECM, BCM, TCM) that might possibly affect the configuration and safety of your car.
IF YOU DO NOT KNOW WHAT YOU ARE DOING, DO NOT ATTEMPT TO MODIFY YOUR CAR!

SETUP:
------
* 1x Raspberry PI 3B, with a default install of Raspbian LITE
* 1x CANBUS PiCAN2  
* 1x OBD -> serial female connector (DB9F)  

SETTING UP THE HARDWARE:
-------------------------
Connect the PiCAN2 board to the Raspberry as per instructions.
Connect the OBD connector onto the serial connector on the PiCAN2 board.
Make sure that your car's ignition is at least in the 'II'-setting.


LIBRARIES:
----------
You will need the following libraries to succesfully run this project:
 1. JAVA, this requires an additional install on raspberry (apt install oracle-java8-jdk)
 2. cansend and candump utilities, please place them in the same folder as this jar

CONFIGURATION:
--------------
In your Raspberry, enable the PiCAN2 board by editing /boot/config.txt:

`dtparam=spi=on` <br/> 
`dtoverlay=mcp2515-can0,oscillator=16000000,interrupt=25`<br/> 
`dtoverlay=spi-bcm2835-overlay`<br/>

Use the following command to bring the can0 interface up:<br/>


`sudo /sbin/ip link set can0 up type can bitrate 500000`

Look up the proper communication speeds for your modelyear car. Mine, a P2 S60 of model year 2009, uses
* 125kbps for the low-speed CANBUS and
* 500kbps for the high-speed CANBUS.

Set the correct speed for this program, noting that it will default to the above values.
This HS-CAN is what you will need for the important stuff: DPF temperature monitoring.

Between different model years, this speed changes. Configuring an incorrect speed will definitely upset your
setup and could possible even DAMAGE YOUR CAR. Always check and double-check your vehicle's communication speeds
before moving on.


Verify you have a can0 (or something like that) interface present on your system. This is the main mode of
communication for the utils mentioned, so it should be up and running.

First run:
--------------
Make sure libsocket-can-java.jar is available by building it (preferably on the target pi-hole) (use -Dmaven.skip.test=true if needed)
Make sure there is a valid target.properties (based off of target.properties.example)
For deploy to raspberry and decentralized run: run maven package 
For local run: run maven package in profile "local-run"

CanSocket.java licensing:
--------------

A copy was taken from https://github.com/entropia/libsocket-can-java
Which falls under the MIT license:
The MIT License (MIT)
Copyright (c) 2012 Hannes Frederic Sowa <hannes@stressinduktion.org>

Permission is hereby granted, free of charge, to any person obtaining a
copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

