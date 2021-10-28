# .await() Blocking Thread Issue

This repository represents a simple test scenario that uses the extension function `.await()`
from [`kotlinx-coroutines-play-services`](https://github.com/Kotlin/kotlinx.coroutines/tree/master/integration/kotlinx-coroutines-play-services)
. The test hangs indefinitely because the main thread is blocked from the `await()` call.
Alternative suggestions are provided in comments next to the troublesome code, such as the use
of `Tasks.await()` in place of `await()`, to make the tests pass.

The purpose of this repository is to:

- Highlight the lack of test examples in the community that use `.await()`
- Provide an easily-reproducible example of the issue in question for debugging purposes.

## Setup

1. Checkout repo
1. [Setup Firebase CLI](https://firebase.google.com/docs/cli)
1. Navigate to project directory and run `firebase emulators:start`

- Note: This may have you download the Firestore emulator on the first run.
- This step will start the Firestore emulator on port 8080, and the Emulator UI on port 4000 (to
  view contents of local Firestore database)

1. Navigate to `RepositoryTest` in Android Studio and run the test.

** Expected **
Test passes

** Result **
Test hangs indefinitely
