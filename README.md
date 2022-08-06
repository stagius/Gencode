# README

## How to deploy to Heroku
The ``master`` branch should be up to date. Then push to ``heroku`` remote :

```bash
git push heroku master
```

---
How to change the ``buildback`` of a Heroku app (just FYI):
```bash
heroku buildpacks:set heroku/java
```