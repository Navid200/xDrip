---
title: "Snapshot Cost on Google Cloud"
description: "Learn what the 'Snapshot Cost' entry means in the Nightscout cost table for Google Cloud — how snapshots impact billing and ways to manage these charges effectively."
layout: GCNS
---

# Google Cloud Storage PD Snapshot  
[Google Cloud Nightscout](./GoogleCloud.md) >> Why have I been charged for Storage PD Snapshot?  
  
A snapshot is a compressed backup of disk data.  For example, a snapshot of a 30GB disk that is about 15% full would be approximately 3.3GB.  [Snapshots](./Snapshots.md) are not free and incur costs. You can review specific pricing details on [Google Cloud’s pricing page](https://cloud.google.com/compute/pricing-announce#storage_pd_snapshot_skus).  A 3.3GB snapshot costs about $0.25 a month.  
  
To minimize expenses: 
- Delete snapshots as soon as you no longer need them.
- [Disable automated snapshots](./GCNS/DeleteSnapshotSchedule.md) if you didn't when you created the virtual machine.  
  
If you need to investigate a charge, the most effective method is to create a [cost table](./CostTable.md) in your Google Cloud account. This will help you track and identify charges related to your snapshots and other resources.  
  
  
