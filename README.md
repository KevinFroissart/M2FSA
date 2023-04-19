### Connect kubectl to remote :

## Windows :
```bash
$Env:KUBECONFIG="C:\Users\kfroissart\.kube\local.yaml"
$Env:KUBECONFIG="C:\Users\kevin\.kube\config.yaml"
kubectl config set-context $(kubectl config current-context) --namespace=prj-15
```

### TP 1
## Commands :
- api create key `kubectl run -it --image curlimages/curl curl --rm --command -- curl -X POST -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" http://api:8081/key/admin/cle`
- api get keys `kubectl run -it --image curlimages/curl curl --rm --command -- curl -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" http://api:8081/key/admin`
- postgres connect `kubectl run -it --image postgres psql --rm --command -- env PGPASSWORD=apidbpwd psql -h postgres -U apidb apidb`

## TP 2
### Commands :
- prometheus port-forward `kubectl port-forward deployment/prometheus-server 9090:9090`

TODO: Modifier la configuration de prometheus pour récupérer les métriques ainsi exportées et vérifier que la récupération fonctionne (par exemple en regardant l’onglet status -> targets ou en affichant la métrique pg_stat).
A lire: https://devopscube.com/setup-prometheus-monitoring-on-kubernetes/