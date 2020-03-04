aws iam list-users
aws iam delete-login-profile --user-name $1
aws iam list-attached-user-policies --user-name $1
aws iam detach-user-policy --user-name $1 --policy-arn <ARN>
aws iam list-access-key --user-name $1
aws iam delete-access-key --access-key-id <AccessKey> --user-name $1
aws iam delete-user --user-name $1
