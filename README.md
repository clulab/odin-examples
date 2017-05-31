# Example Domains for `odin`

This project provides an introduction on how to use `odin` with several simple domains. Please see the [Odin Wiki](https://github.com/clulab/processors/wiki/ODIN-(Open-Domain-INformer)) for more details about our event extraction framework, including a complete manual of its rule language and API.

## What you'll need...
  1. [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
  2. [sbt](http://www.scala-sbt.org/release/tutorial/Setup.html)

## Example domains

The domain examples are found under [`toydomains`](src/main/scala/toydomains).  The corresponding grammars should be under [`resources/grammars`](src/main/resources/grammars).

Run any of the examples with `sbt run`

|__Domain__ | __Description__|
|--------|----------------|
|[`bio`](src/main/scala/toydomains/bio) | A very simple introduction that uses the biology domain |
| [`likelihood`](src/main/scala/toydomains/general) | A hedging grammar, i.e., how likely or reliable is an event mention?  In this example, we look at how we might label events in terms of their likelihood.  This example also shows how to use a grammar with a master file, taxonomy, imports, and variables. |

## Project skeletons

Ready to get serious?  Use one of these branches to start your own project with `odin`.

|__Branch__ | __Description__|
|--------|----------------|
| `skeleton` | A bare-bones project for you to fork |

## Contribute

Help us spread the good word.  Fork this repo, develop your own example project, and put in a pull request.
