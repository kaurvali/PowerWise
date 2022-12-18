# PowerWise #
### Mobile App Development LTAT.06.021 - Projects
Kaur Vali, Siim Saaresalu, Merili Kõvask, Märten Kahu

### Github: https://github.com/kaurvali/PowerWise

### What is PowerWise?
PowerWise is a simple application to help you keep track of Nord Pool Spot electricity prices in
Estonia.

It features a simple home page to display electricity prices for the day with an option to look at 
the prices for the next day or the previous ones. 

It also features an calculator, where you can calculate how much it will cost to use some sort of a 
device, which consumes electricity. 

There is a simple widget, which you can use to check the current electricity prices on go without
having to open up the application itself.

### How to install the application
It is really simple. Clone the application, make the gradle sync and run it either on your phone
or on the emulator. It has multiple dependencies, like Room, ION, Material3, JetPack Compose, AppWidget
and it needs access to the internet to communicate with the API.
- MinSDK: 27
- TargetSDK: 33
It has been tested on emulated Pixel 4 on API lvl 33.

### Functional Blocks

#### Which functional feature blocks does the project fulfil?
The project currently has 4 functional blocks.
1. Android App Widgets
2. RoomDB
3. Material3 with JetPack Compose
4. Web API (Elering Nord Pool Spot API)

#### What other technologies, libraries you used in your project.
There are multiple different technologies used, most of them are reflected in the functional blocks.
We have used Ion to get data from an API. RoomDB to store data in the DB locally so that the price
can be fetched instantly and kept on the device. Material 3 with JetPack Compose to design the UI
better and to use blocks instead of views in the XML files. Also AppWidget is used to create the
widget which could be used on the home screen.

#### Who worked on what? 
- Kaur: Built the communication with API and the data flow and logic to the DB. Most of the UI
elements and chart which displays prices.
- Siim: Calculator
- Merili:
- Märten: Widget, refining the DB communication and application flow.

#### What went well? 
We think that the application itself came out great. The idea was great and thanks to that it was
quite good to build a well functioning application. Application itself looks great and works well
regarding the UX part.

#### What went south?
There are two things which could have been done better. We could have thought out the data flow and
architecture out a bit better. Currently we just started it without thinking about Material3 and
architecture of the application which meant we run into problems and had to redo some things. This 
also means that the data flow and logic is not perfect. Next time this should be the step 1, to
understand how the data should flow.
Second thing is time management. We always felt like we had a lot of time but when the time came
we ended up in a rush and thanks to that some of the features which we wanted to implement were not 
done.

#### What would you add/do if you had more time?
An system to give an notification at a certain time of the day which will notify the user about the 
prices of the Electricity for the next day. Also improve the UX a little bit more and add more 
finishing touches to the UI.

#### What was the most challenging problem?
I think the first big challenge was creating the Jetpack composeapplication as this was not taught 
in the course and had big changes in logic compared to the old one which made it really difficult to
implement. The other really challenging one was the widget.


