---
layout: GCNS
---

## Upgrade to Ubuntu 24  &nbsp; &nbsp; **<span style="color:red">DRAFT</span>**  
[Google Cloud Nightscout](../GoogleCloud.md) >> Upgrade to Ubuntu 24  
<br/>  
  
We continuously update the setup and provide an [update mechanism](../NS_SyncExecutables.md) for seamless updates.  
However, some updates—such as upgrading to Ubuntu 24—cannot be performed using this mechanism.  
The following instructions will guide you through the upgrade process.  
<br/>  
  
1- Update your existing setup by following [these instructions](../NS_SyncExecutables.md).  
  
2- Create a [backup and transfer it to your computer](../DatabaseBackup.md).  
<br/>  
  
3- (Optional) Create a [snapshot](../Snapshots.md).  Note: Snapshots are not free.  
<br/>  
  
4- (Optional) Set up a second Google account for a new installation:  
- Follow our [installation instructions](../GoogleCloud.md).  
- [Restore your backup](../DatabaseRestore.md) to the new installation.  
- (Optional) Continue using this new installation and retire the virtual machine in your original Google account.  
<br/>  
  
5- If you intend to continue using your original Google account:  
- Ensure you have created and securely stored your backup.  
- [Delete your original virtual machine](../DeleteVM.md).  
- Follow our [installation instructions](../GoogleCloud.md) to set up a new Ubuntu 24 virtual machine.  
- Complete all installation steps and [restore your backup](../DatabaseRestore.md).  
  
