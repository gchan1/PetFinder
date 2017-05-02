# schema for database

Please suggest any improvements/additions and we can work to seamlessly integrate them.

## organization

We will be using Firebase which is a NoSQL database. Thus unlike assignment 2, we will not be using tables with columns and rows to organize the information. It will be a nested JSON tree. See the following for tips and information about how to best build a NoSQL database: https://firebase.google.com/docs/database/web/structure-data

## schema
There will be two databases. One holding information about the dog, and the other will hold information about the shelters.

Each "dog" will be nested with the following information
```
dog_id
  -name
  -shelter_id
  -Age
  -Gender
  -Type of Dog
  -Description of Puppy
```
  
Each shelter will be nested with the following information. Note, this is in a seperate datase than dog_id. The shelter_id value in dog_id only contains the value of shelter_id and not the nested information that I am about to list.
```
shelter_id
  -name
  -latitude
  -longitude
  -Address
  -Contact info
  -Phone Number
  -Open Hours
  -dogs
     -[dog_id[0]...dog_id[n]] for n dogs in the shelter
 ```    


## Implementation

For our android app, there should only be reading from the database since this is a user facing application. I am working through the exact code to read information and will place it here as I progress.  
