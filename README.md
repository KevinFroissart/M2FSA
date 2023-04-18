### Connect kubectl to remote :

## Windows :
```bash
$Env:KUBECONFIG="C:\Users\kfroissart\.kube\local.yaml"
$Env:KUBECONFIG="C:\Users\kevin\.kube\config.yaml"
kubectl config set-context $(kubectl config current-context) --namespace=prj-15
```