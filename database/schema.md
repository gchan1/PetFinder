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

## Tutorial

I will now explain how to simply adjust your code to use the Firebase Database. For this I have created Java Classes to help parse the data (ShelterInformation.java, EventInformation.java, PetInformation.java). Just make sure these classes are in the same directory as the activity you are calling it from. 

I will now go through each step and explain what they do. In the MainActivity file you can find the same title of each section as comments so you can find the part of the code I am referencing.


### Step-1: Declare Variables
```
private DatabaseReference mShowPets;
private DatabaseReference mShowShelters;
```
Firebase uses DatabaseReference to call link our Android phone to the database. Declare as much as you need. 

### Step-2: Set which part of the database the listener will refer to
```
mShowPets = FirebaseDatabase.getInstance().getReference().child("pet_id");
mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");
```

For each DatabaseReference, it will reference the entire database unless you specify which child node to focus on. Here we set one DatabaseReference to the pet_id branch of the database, and the other to the shelter_id part. 

### Step-3: Declare each Value event Listener (need one for each database reference)

```
ValueEventListener showPetsListener = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PetInformation[] petList = showPets(dataSnapshot);
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
        Toast.makeText(MainActivity.this, "Failed to load post.",
        Toast.LENGTH_SHORT).show();
    }
};  
```
Firebase uses ValueEventListener to handle the events that happen during the connection with the database. There are only two:
- The first, onDataChange, is called as soon as a connection is established or when someone changes the part of the database that is referenced. 
- The second, onCancelled, is called when an internal error happens within the database. Since we are only doing reads, you can expect this to not be called within our application.

Note we need to create a ValueEventListener for each database reference, see my MainActivity code to see how I created the one for the shelter_id database reference. There are only two line changes.

### Step-4: Add ValueEventListener to our database reference 
```
mShowPets.addValueEventListener(showPetsListener);
mShowShelters.addValueEventListener(shelterListener);
```
This simply adds our created ValueEventListener to their respective DatabaseReference.

### Step-5: Parse the dataSnapshot
```
private ShelterInformation[] showShelter(DataSnapshot dataSnapshot){

        for(DataSnapshot ds: dataSnapshot.getChildren()){
        //Parse ds here
    }
    ...
}
```
We will now parse the DataSnapshot which contains all the value and keys of the DatabaseReference we set. I have already created Classes for this task and have done all the parsing of the DataSnapshot. If you refer to the part of the code, just copy what already wrote. 

### Step-6: Use data
What returns from our parsing is a List of Classes with our onDataChange function in our respective ValueEventListener (see Step-3 for this location). For pet information, we get back a list of of PetInformation classes. You can then use this list within your Android activity accordingly. Note each Class has getter functions that you use to pull data from each. 

Also note that the ShelterInformation class has a PetInformation List embedded within it. Please see each respective Java class for all the information held within the class and the getter functions you need to use. 

### End

Please contact me if you need help with understanding any of this. I can also help create specific data structures to help instead of a List of Classes. 

I heavily commented the code with these steps and more information and I hope it is helpful!
