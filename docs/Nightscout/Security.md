---
title: "Nightscout Security on Google Cloud"
description: "Learn about the security architecture of Google Cloud Nightscout, including VPS hardening, automatic patching, and attack surface protection. Documentation and insights from the lead developer."
layout: GCNS
---

# Google Cloud Nightscout & security
[Google Cloud Nightscout](./GoogleCloud.md) >> Security  
  
There have been questions about how robust Google Cloud Nightscout may be compared to alternatives.  We reached out to our lead developer, JamOrHam, for advice and the following was his response.  
<br/>  
  
---   
  
In terms of security, the google cloud vps only has web port open. Google manage the ssh keys. The web port has a nginx front-end using openssl and node-js.  
These are the main critical attack surfaces and these components power half of the internet and we have automatic updates enabled for them.  
So, any vulnerability will be automatically patched.  
They are very widely tested and mature products with very strong security.  
  
Then there is nightscout and the modules it contains.  
Any vulnerability there would basically be the same for any nightscout installation and if there were a critical vulnerability, we would expect to hear about it.  
It could also be patched.  
  
The most likely issue would be password security which is also the same for any method of deploying nightscout and the responsibility of the user.  
  
I seriously doubt that any random hacker who managed to defeat all of those defenses would even be interested in delivering a remote bolus maliciously.  
The only people who would be interested in that would be someone known to the user or security researchers.  
Random hackers would be far more interested in trying to steal money or send spam.  Google totally block all outbound email and we do not use any cloud credentials on the vps so they can't use the vps to set up more vps for bitcoin mining or something.  
  
It is definitely worth making sure we haven't made any mistakes with the installation but as far as I know the nightscout authentication controls work and so I'm not sure what else we would need to check.  
