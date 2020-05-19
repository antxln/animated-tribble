# animated-tribble

## Data Persistence

Room sets up and configures a local database to store data even after the app closes. Room is an abstraction layer over an SQLite database.
SQLite implements an SQL database engine and has the following characteristics:
- Self-contained (requires no other components)
- Serverless (requires no server backend)
- Zero-configuration (does not need to be configured for your app)
- Transactional (changes within a single transaction in SQLite either occur completely or not at all)

The three major components of Room are database, data access object (DAO), and entity (database table).
The database uses the Singleton pattern to return an instance of the database. The variable storing the instance is annotated `@Volatile`, to indicate the variable is updated immediately on read/write and not cached. This ensures the value is up to date for all execution threads.
Room database supports exporting schema (for a history of database versions), and requires a migration strategy (method for transferring existing data into database with new schema).
DAOs contain Kotlin function to SQL query mappings, which allows database interactions using Kotlin functions.

## Interaction with Database

User-facing application often has a main thread (or UI thread) which updates UI and handles click handlers. For a smooth user experience, UI needs to be updated at about 16 frames per second, to prevent visible pauses. Therefore, it is critical to not block the main thread. If the main thread is blocked for too long, the application will crash.
In a large database, accessing and updating data is a long running task. Other examples of long running tasks are those dealing with networks and files.

A method for performing long running tasks without blocking the main thread is callbacks. Long running tasks are done in a background thread, and the main thread is notified on completion.
However, programs that use callbacks are hard to follow (i.e. debug), as the flow is not sequential. In addition, callback style is unable to use certain language features (e.g. exceptions, as program flow has left function that registered the callback)

Kotlin has coroutines, converts callback-based code to sequential code (with `suspend`), and can use exceptions.
`suspend` marks function available to coroutines, where execution may be paused and resumed later (does not need to run until completion). The function may continuously pause and continue, until completion, without blocking the thread. The suspended function may run on any thread, not necessarily on the thread it was called.

Coroutines need a job, dispatcher, and scope.
Coroutines belong to jobs, which may be cancelled to stop coroutines. Jobs have a parent-child hierarchy, where cancellation of parent results in cancellation of all children (easy to maintain). By default, failure of any child also results in immediate failure of parent. Jobs are launched for side-effects, and do not produce result values. See [Deferred](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-deferred/index.html) interface for a job that produces a result.
The dispatcher determines what thread(s) the coroutine runs on. Coroutine may be confined to specific thread, a thread pool, or run un-confined.
Scope combines dispatcher and job, and defines context where coroutine runs.

## Feedback to User

Snackbar is the preferred method for displaying feedback information, relevant in the activity, to users.
Toasts are used for system-level notifications, as they persist even when activity terminates.

## Misc.

### Re-using views and layouts

Components may be re-used by defining the component in a separate layout file, and using the `<include />` tag in other layouts. As with other widgets, the component is further customizable in the layout with attributes.

The `<merge />` tag helps eliminate redundanct view groups when including one layout within another. For example, if the reusable layout is a vertical LinearLayout, and it is included in another vertical LinearLayout, there is now a vertical LinearLayout inside another vertical LinearLayout. The nested LinearLayout serves no real purpose and slows down UI performance.
The `<merge />` tag is used as the root view of the reusable component, and when the component is included in another layout, it assumes the layouts root.