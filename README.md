# Getting Started

### Run the application locally 

Once the jar is running the app is set to be accessible using `localhost:8088/swagger-ui.html`.

To use the `/hand` endpoint you can type the card values in directly e.g. '3D 3C 2D 2S 6D'.

To use the `/upload` endpoint you simply need to select the file. 

### Tests

The integration tests are housed under the folder `integration`. All other tests are unit tests,
except `PokerHandControllerTests` which straddles the divide.

### Current position

Time is unfortunately a factor so I am submitting this without having completed all the refactoring I wanted. I intend 
to complete it this evening, but wanted to get this out for viewing.

The core requirement has been met (per https://en.wikipedia.org/wiki/Poker_probability), the 'hand' tests correctly 
identify the hand types based on their 'Frequency' values and a clunky json response is returned.

Classes that need additional tests (though are not part of the core requirement) are:

`ErrorControllerAdvice`, `PayloadValidator` (is indirectly tested via the Controller and integration tests).
The `HandType` enum is indirectly tested via the integration tests and it offloads the actual
implementation of the card identification to `IdentifyHelper` which has complete tests for the 
behaviour bits (but currently lacks basic parameter validation).
