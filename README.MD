# Revel Android Interview Project

We'd like you to recreate a basic version of the Revel Android App. 
Your app should show vehicles on a map based on their location. 
A user should then be able to reserve, ride, pause and end their ride on the app. 
We started things off with a basic setup in `MainActivity` and a vehicles response file in `raw/upstream_vehicles.json`.

## Requirements:

- Show the vehicles on the map.
- A user should be able to (1) reserve, (2) ride, (3) pause, and (4) end their ride on the app. A user can start a ride without a reservation. Or they can reserve the vehicle before riding so someone else doesn't take the vehicle. If they change their mind, they are allowed to end a reservation before starting their ride. While riding, they are allowed to pause the ride and resume or end it.
- Your UI should show the current state of the ride and the actions available for it. (See the sample screenshots for base requirements)

### Questions?

- Is the call out window after selecting a vehicle on the map I see in the screenshots required? No, it is just an example, it is not required.
- Does it have to look like the screenshots? No. The important thing is that the UI reflects the current state and possible actions.

### Drilling down on the user stories 

- Show the vehicles on the map.
        Parse json raw file on map load
        Click on a vehicle and show status
            If the vehicle has no state, they can reserve OR start ride
            If the vehicle is reserved, they can start ride OR cancel reservation
            If the vehicle is in the middle of a ride, they can pause OR end ride
            If the vehicle is in a paused, ride they can resume ride OR end ride 
        Change icon color on mapbox
        
- A user should be able to 
(1) RESERVE
        A user can reserve the vehicle before riding so someone else doesn't take the vehicle
        A user is allowed to end a reservation before starting their ride, if they change their mind 
(2) RIDE 
        A user can start a ride without a reservation
        While riding, they are allowed to pause the ride and resume or end it.
(3) PAUSE 
        While riding
(4) END THEIR RIDE 
        While riding
        
- Your UI should show the current state of the ride and the actions available for it. 
(See the sample screenshots for base requirements)        
        
### Things I'd Do Next Time Around
- With a larger application I would make more modular subcomponents and activity modules for dagger
- Add additional caching infrastructure, checking to see if we already have converted file to objects and/or storing converted objects locally
- Button select states and custom reusable views


