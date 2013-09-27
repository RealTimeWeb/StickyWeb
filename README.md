StickyWeb
=========

A module for uniformly accessing distributed resources and, more powerfully, pre-cached distributed resources.


Java
====

    StickyWeb connection = new StickyWeb();
    StickyWebResponse response= connection.get("url", ...).setOnline(false).execute();
    System.out.println(response.asText());
