# demo-microservices

Contains microservices that handle voting on different programming languages and then persisting the information in a
database. These applications are SpringBoot-based and packed inside a Docker container.

# TODOs

* Change no-testing database to Postgres instead of H2.
* Add containerization code.
* Replace RestTemplate (especially if SpringBoot 5 is used) with Feign Client.
* Add cloud configuration.
* Add integration tests.
* Make dependencies as much explicit as possible.
* Add front-end code.
