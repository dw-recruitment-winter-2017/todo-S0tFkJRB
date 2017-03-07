# To Do List

This is a simple to do list, written using Pedestal, Datomic, Reagent, and the
Bulma CSS framework.  When running in dev mode, the main app is accessible at
`/index.html`.

I've included some helpers to set up the dev environment in a REPL (I used
[boot](http://boot-clj.com) to set up the build, so use `boot repl`).

### Setting up your REPL

```clojure
(require 'server 'repl 'fecfg)
```

### Starting the dev server

```clojure
(server/start-dev)
```

### Restarting the dev server

```clojure
(repl/restart) ;; also reloads several modules and rebuilds the database
```

### Building the frontend

```clojure
(fecfg/build)
```

### Watching for frontend changes

```clojure
(fecfg/watch)
```

---

The dev server loads some sample data into the database.  Notably, it
hardcodes an ID for one of the to do entries
(`58bd2b73-4349-4726-83bc-9804fe74b125`) for easy testing.

Depending on your frontend deployment, you may need to update CORS settings.
Modify `service-map` in `server.clj` to include `::http/allowed-origins`,
similar to how it's set in `start-dev`.

Database configuration settings (the Datomic URI) are in `dbcfg.clj`, which
also provides a few management functions (`(require 'dbcfg)`).

Speaking of Datomic, I used the "free" version.  I'm not sure if this makes a
difference, but FYI.

## Pitfalls

For some reason boot wants to load Clojure 1.7.0 on my machine.  Running it
with `BOOT_CLOJURE_VERSION=1.8.0 boot repl` solves it; otherwise I get a
`NoClassDefFoundError: clojure/lang/Tuple` error.

## To Add

As a demo there are a number of things missing.  There's no CSRF protection
(unless Pedestal is working magic I don't know about).  There is no limit on
todo data length or number of todos.  Or fun stuff!  Like the first time you
complete a todo there's a cool star shower or something.
