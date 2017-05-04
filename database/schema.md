# Schema for database

Please suggest any improvements/additions and we can work to seamlessly integrate them.

## Organization

We will be using Firebase which is a NoSQL database. Thus unlike assignment 2, we will not be using tables with columns and rows to organize the information. It will be a nested JSON tree. See the following for tips and information about how to best build a NoSQL database: https://firebase.google.com/docs/database/web/structure-data

## Schema
There will be two databases. One holding information about the dog, and the other will hold information about the shelters.

Each "dog" will be nested with the following information
```
pet_id
  -image
  -name
  -shelter_id
  -Age
  -Gender
  -Breed
  -Description of Pet
```
  
Each shelter will be nested with the following information. Note, this is in a seperate datase than dog_id. The shelter_id value in dog_id only contains the value of shelter_id and not the nested information that I am about to list.
```
shelter_id
  -name
  -image
  -latitude
  -longitude
  -Address
  -Contact info
  -Phone Number
  -Open Hours
  -events
    -Latitude
    -Longitude (For putting on the map)
    -address
    -Start Time
    -End Time
    -date
    -name
    -moreInfo (just a link to the event's information online)
    -
  -dogs
     -[pet_id[0]...pet_id[n]] for n pets in the shelter
 ```    
Note: We will only have events in the database if they only last for one single day. If not time specific, just set Start and End Times to null. Also, Lat/Long does differ between shelters and events.

## Implementation

For our android app, there should only be reading from the database since this is a user facing application. I am working through the exact code to read information and will place it here as I progress.  

In Firebase there exists no lists. See this [great blog post for why](https://firebase.googleblog.com/2014/04/best-practices-arrays-in-firebase.html). That is why the list of pet id's in shelter has key value pairs.

To read from the Firebase Database, it helps to create a class to access each JSON branch of data. See [this tutorial](https://youtu.be/2duc77R4Hqw?t=5m17s) starting at 5:17 to get a great explanation for how to build this. 
