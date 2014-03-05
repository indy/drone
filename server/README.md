# drone-backend

A simple clojure app to generate the json files which are requested by clients.
The source data originally came from a cron job which polled http://api.dronestre.am/data for json. This has been replaced by resources/canonical.json, a file under source control which is managed manually.
The reasons for this were:
  - slow updates to the dronestream api data (~month out of date)
  - incorrect data

A human (me) will have to monitor the dronestre.am feed, twitter and the bureau of investigative journalism in order to maintain the feed. Hopefully the results should be worth it.

## Usage

build the jar file with:
  lein uberjar

execute refresh.sh script to output files into the ./cache directory

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
