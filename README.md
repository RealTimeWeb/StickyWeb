StickyWeb
=========

A module for uniformly accessing distributed resources and, more powerfully, pre-cached distributed resources.


Java
====

        StickyWeb connection = new StickyWeb();
        StickyWebRequest request= connection.get("url", ...);
        StickyWebResponse response = request.setOnline(false).execute();
        System.out.println(response.asText());
