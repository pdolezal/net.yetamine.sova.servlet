# net.yetamine.sova.servlet #

This repository provides an extension of [net.yetamine.sova](http://github.com/pdolezal/net.yetamine.sova) library for Java Servlet API. Define attributes of your requests and context parameters in a type-safe way!


## Examples ##

Let's take a classical example with X.509 certificates from a request:

```{java}
// Define such a constant for the attributes that you want to access
public static final ServletRequestSymbol<X509Certificate[]> CERTIFICATES
= new ServletRequestSymbol<>("javax.servlet.request.X509Certificate", Downcasting.to(X509Certificate[].class));

// Use the constant defining the key to get the request attribute
final X509Certificate[] certs = CERTIFICATES.get(servletRequest);

// This is an effective replacement for:
// final X509Certificate[] certs = (X509Certificate[]) servletRequest.getAttribute("javax.servlet.request.X509Certificate");
```

The example shows how using the typed key relieves the user from casting and remembering the exact attribute name, or when defining a constant fo the name, allows coupling the name with the type right away, with proper documentation of the entry that the name refers to.


## Prerequisites ##

For building this project is needed:

* JDK 8 or newer.
* Maven 3.3 or newer.

For using the built library is needed:

* JRE 8 or newer.


## Acknowledgments ##

A special thank belongs to [Atos](http://atos.net/). The development of this library would be much slower without their support which provided a great opportunity to verify the library practically and improve it according to the experience.

Another thank belongs to *davej* from [project77.org](http://project77.org/) for the permission to use his owl picture as the logo for this project. Why an owl? Because it is so cute and because *sova* means *an owl* in Czech.


## Licensing ##

The project is licensed under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0). Contributions to the project are welcome and accepted if they can be incorporated without the need of changing the license or license conditions and terms.


[![Yetamine logo](https://github.com/pdolezal/net.yetamine/raw/master/about/Yetamine_small.png "Our logo")](https://github.com/pdolezal/net.yetamine/blob/master/about/Yetamine_large.png)
[![Sova logo](https://github.com/pdolezal/net.yetamine.sova/raw/8677011f54f4fcfda8be987a461f8109bfbd1308/about/sova_tiny.png "Project logo")](https://github.com/pdolezal/net.yetamine.sova/blob/8677011f54f4fcfda8be987a461f8109bfbd1308/about/sova_large.png)
